const e = require('express');
const mongoose = require('mongoose');
const Game = require('../game/gameModel');

const Schema = mongoose.Schema;

const usuarioSchema = Schema({

  nombre: {
    type: String,
    required: true
  },

  password: {
    type:String,
    required: true
  },

  image: {
    type: String,
    default: null
  },

  games_followed: [{
    type: mongoose.Schema.Types.ObjectId,
    ref: 'Game',
  }]
  
},{
    timestamps: true
});

const Usuario =  mongoose.model('Usuario', usuarioSchema);

module.exports = Usuario;