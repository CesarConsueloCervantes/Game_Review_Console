const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const consoleSchema = Schema({

  console_name: {
    type: String,
    required: true
  }

},{
    timestamps: true
});

const Console =  mongoose.model('Console', consoleSchema);

module.exports = Console;