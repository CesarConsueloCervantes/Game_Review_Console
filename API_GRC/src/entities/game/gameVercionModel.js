const mongoose = require('mongoose');
const mapPercentageToReviewRate = require('../../utils/mapPercentageToReviewRate');
const Game = require('./gameModel');
const Console = require('../consol/consoleModel');
const calculateScore = require('../../utils/calculateScore');

const Schema = mongoose.Schema;

const gameVercionSchema = Schema({

  game: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'Game',
    required: true
  },

  game_name: {
    type: String
  },

  console: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'Console',
    required: true
  },

  console_name: {
    type: String
  },

  image_vercion: {
    type: String,
    default: ""
  },

  review_rate:{
    type: String,
    enum: ['None', 'Negative', 'Mostly Negative', 'Mixed', 'Mostly positive' , 'Positive'],
    default: 'None'
  },

  review_percent:{
    type: Number,
  }, 

  review_count_recomendated: {
    type: Number,
    default: 0
  },
  
  review_count_not_recomendated: {
    type: Number,
    default: 0 
  },

  review_count_mixed:{
    type: Number,
    default: 0 
  }
},{
    timestamps: true
});

gameVercionSchema.pre('save', async function(next) {
  try {
    if (!this.game_name) {
      const gameDoc = await Game.findById(this.game);
      if (gameDoc) {
        this.game_name = gameDoc.game_title;
      }
    }
    if (!this.console_name) {
      const consoleDoc = await Console.findById(this.console);
      if (consoleDoc) {
        this.console_name = consoleDoc.console_name;
      }
    }
    next();
  } catch (error) {
    next(error);
  }
});

gameVercionSchema.post('findOneAndUpdate', async function (doc, next) {
  try {
    if (!doc) return next(); // nada que hacer

    const positivos = doc.review_count_recomendated ?? 0;
    const negativos = doc.review_count_not_recomendated ?? 0;
    const mixtos    = doc.review_count_mixed ?? 0;

    console.log("positivos:", positivos, "negativos:", negativos, "mixtos:", mixtos);

    const score = calculateScore(positivos, negativos, mixtos) * 100;

    const newRate = mapPercentageToReviewRate(score);

    // actualizar valores derivados
    await doc.updateOne({ 
      review_percent: score, 
      review_rate: newRate 
    });

    next();
  } catch (err) {
    next(err);
  }
});


const GameVercion =  mongoose.model('GameVercion', gameVercionSchema);

module.exports = GameVercion;

