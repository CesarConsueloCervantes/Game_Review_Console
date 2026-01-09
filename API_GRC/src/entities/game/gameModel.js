const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const gameSchema = Schema({

  game_title: {
    type: String,
    required: true
  }

},{
    timestamps: true
});

const Game =  mongoose.model('Game', gameSchema);

module.exports = Game;