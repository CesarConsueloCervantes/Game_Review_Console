const mongoose = require('mongoose');
require('dotenv').config();

const connectDB = async () => {
  const dbConnection = process.env.MONGODB_URI || 'mongodb://localhost:27017/test';

  try {
    await mongoose.connect(dbConnection);
    console.log('Conexión exitosa a la base de datos');
  } catch (error) {
    console.error('Error al conectar a la base de datos:', error.message);
    process.exit(1);
  }
};

module.exports = connectDB;