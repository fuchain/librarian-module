import fs from "fs";
import path from "path";
import Sequelize from "sequelize";

import env, { checkEnvLoaded } from "@utils/env";

checkEnvLoaded();
const { dbHost, dbUser, dbPass, dbName, dbDialect } = env;

const sequelize = new Sequelize(dbName, dbUser, dbPass, {
    host: dbHost,
    dialect: dbDialect,
    define: {
        paranoid: false,
        underscored: false,
        timestamps: false,
        freezeTableName: false
    }
});

const basename = path.basename(module.filename);
const db = {};

fs.readdirSync(__dirname)
    .filter(file => {
        return file.indexOf(".") !== 0 && file !== basename;
    })
    .forEach(file => {
        if (file.slice(-3) !== ".js") return;
        const model = sequelize["import"](path.join(__dirname, file));
        db[model.name] = model;
    });

Object.keys(db).forEach(function(modelName) {
    if (db[modelName].associate) {
        db[modelName].associate(db);
    }
});

db.sequelize = sequelize;
db.Sequelize = Sequelize;

export default db;
