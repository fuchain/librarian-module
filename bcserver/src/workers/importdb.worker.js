import Queue from "bull";
import env from "@core/env";
const importQueue = new Queue("importDB", `redis://${env.redisHost}`);

// Dependency to run this queue
import { db } from "@core/db";
import axios from "axios";

// Watch and Run job queue
function run() {
    importQueue.process(jobCallback);
}

// Describe what to do in the job
async function doJob(id) {
    const axiosConfig = {
        headers: {
            Authorization:
                "Bearer " +
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsaWJyYXJpYW5AZmUuZWR1LnZuIiwicm9sZXMiOlsibGlicmFyaWFuIl0sImV4cCI6MTU3NzgzNjgwMH0.I5DEYij7PfMo7nqcDARiBWmQT91Yv9R5Pi-9U82_PTNdI_mm9btvm9QGPh8CsO7Bs76SRkp7JNE8-VtzrjuA1g"
        }
    };

    try {
        const { data } = await axios.get(
            "https://lapi.fptu.tech/api/v1/bookdetails/" + id,
            axiosConfig
        );

        // Refactor book_detail entity, everything must be snake_case in output
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
        const collection = db.collection("book_details");

        await collection.insertMany([bookDetail]);
        console.log(`Success inserted book_detail ${id}!`);

        return id;
    } catch (err) {
        if (
            // Check if the legacy server return 404 (id not found)
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

async function jobCallback(job, done) {
    await doJob(job.data.id);
    done();
}

async function addJob() {
    // Clean the queue
    console.log("Cleaning the importDB job queue...");
    await importQueue.clean(1000);

    // There are 2151 rows in db so we will create 2151 jobs :)
    const loopTime = 2151;

    // For loop write in declarative way, we will create a array that have 2151 element
    const loop = Array.from(Array(loopTime));

    // Create job promise
    loop.map((_, i) => {
        const index = i + 1;

        // This wil return a promise
        return importQueue.add({
            id: index
        });
    });

    // Waiting for add all job (2151 jobs) is done, and return
    await Promise.all(loop);
    return true;
}

export default {
    run,
    addJob
};
