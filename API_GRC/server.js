const express = require('express');
const connectDB = require('./src/config/database');
const routes = require('./src/entities/index');
const errorHandler = require('./src/middlewares/errorHandler');
const databaseErrorHandler = require('./src/middlewares/databaseErrorHandler');

const app = express();
const port = process.env.PORT || 3005;

async function main() {
    try {
        await connectDB();
        app.use(express.json());

        app.get('/', (req, res)=>{
            res.send('Welcome to API Game Reviews Consoles!');
        })
        app.use('/api', routes);
        app.use(databaseErrorHandler);
        app.use(errorHandler);
        app.listen(port, () => {
            console.log(`🚀 Server listening on port ${port}`);
        });
    } catch (error) {
        console.error('Failed to start server:', error);
        process.exit(1);
    }
}

main();