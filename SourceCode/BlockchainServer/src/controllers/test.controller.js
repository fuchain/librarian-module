import transferService from "@services/transfer.service";
import outputService from "@services/output.service";
import bookService from "@services/book.service";

import { db } from "@models";
import axios from "axios";

const kue = require("kue"),
    queue = kue.createQueue();

async function create(req, res) {
    const body = req.body;

    try {
        res.send(await transferService.createTestBook(body.public_key));
    } catch (err) {
        res.status(400);
        res.send({
            message: err
        });
    }
}

async function transfer(req, res) {
    const body = req.body;

    try {
        res.send(
            await transferService.transferTestBook(
                body.asset_id,
                body.public_key
            )
        );
    } catch (err) {
        res.status(400);
        res.send({
            message: err
        });
    }
}

async function sign(req, res) {
    const body = req.body;

    try {
        res.send(transferService.signTx(body.tx, body.private_key));
    } catch (err) {
        res.status(400);
        res.send({
            message: err
        });
    }
}

async function post(req, res) {
    const body = req.body;

    try {
        res.send(await transferService.postTx(body.tx));
    } catch (err) {
        res.status(400);
        res.send({
            message: err
        });
    }
}

async function getSpent(req, res) {
    const body = req.body;

    try {
        res.send(await outputService.getSpent(body.public_key));
    } catch (err) {
        res.status(400);
        res.send({
            message: err
        });
    }
}

async function getUnspent(req, res) {
    const body = req.body;

    try {
        res.send(await outputService.getUnspent(body.public_key));
    } catch (err) {
        res.status(400);
        res.send({
            message: err
        });
    }
}

async function searchBook(req, res) {
    const body = req.body;

    try {
        res.send(await bookService.searchBook(body.book_id));
    } catch (err) {
        res.status(400);
        res.send({
            message: err
        });
    }
}

// Import one
async function importOne(id) {
    const axiosConfig = {
        headers: {
            Authorization:
                "Bearer " +
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsaWJyYXJpYW5AZmUuZWR1LnZuIiwicm9sZXMiOlsibGlicmFyaWFuIl0sImV4cCI6MTU2MjUwNTc1MH0.s6-e0IpvGIm2Zkjua9qDBZQdN-Junx3aTGiI7KJWYiwXq-zf2gkLNTQx9kr40ubb5Q476afe6hRWghQ-R7buqQ"
        }
    };

    try {
        const { data } = await axios.get(
            "https://lapi.fptu.tech/api/v1/bookdetails/" + id,
            axiosConfig
        );

        const bookDetail = {
            id: data.id,
            name: data.name.trim(),
            categories: data.categories.name,
            authors: data.authors.map(e => e.name),
            libol: data.libol,
            subject_codes: data.parseedSubjectCode,
            preview_link: data.previewLink,
            thumbnail: data.thumbnail,
            published_date: data.publishedDate,
            description: data.description,
            isbn: data.isbn,
            amount: data.book_amount
        };

        // Run query
        const collection = db.collection("tests");

        await collection.insertMany([bookDetail]);
        console.log(`Success inserted book_detail ${id}!`);

        return id;
    } catch (err) {
        if (
            err.response &&
            err.response.status &&
            err.response.status === 404
        ) {
            console.log(`Instance ${id} not found!`);
        } else {
            console.log(`Something failed: ${err}`);
        }
    }
}

async function importJob(job, done) {
    await importOne(job.data.id);
    done();
}

async function createJobs() {
    const loopTime = 2151;
    const loop = Array.from(Array(10));
    loop.map((_, i) => {
        const index = i + 1;
        return queue.create("importDb", { id: index }).save(function(error) {
            if (error) console.log(error);
        });
    });

    await Promise.all(loop);
    return true;
}

async function dbTest(_, res) {
    try {
        await createJobs();
        queue.d;
        queue.process("importDb", 2, importJob);

        res.send({
            message: "Done!"
        });
    } catch (err) {
        res.status(400).send({
            message: err
        });
    }
}

export default {
    create,
    transfer,
    sign,
    post,
    getSpent,
    getUnspent,
    searchBook,
    dbTest
};
