const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const reviewSchema = Schema({

  review: {
    type: String,
    required: true,
    enum: ['recommended', 'mixed', 'not_recommended']
  },
  
  comment: {
    type: String,
    required: true,
    maxlength: 500
  },

  usuario: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'Usuario',
    required: true
  },

  game_version: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'GameVercion',
    required: true
  }

},{
    timestamps: true
});

const Review =  mongoose.model('Review', reviewSchema);

module.exports = Review;