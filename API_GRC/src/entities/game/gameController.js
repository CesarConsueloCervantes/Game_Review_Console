const Game = require("./gameModel");
const GameVercion = require("./gameVercionModel");
const Review = require("../review/reviewModel");

exports.getGames = async (req, res, next) => {
    try {
        const games = await Game.find();
        res.status(200).send(games);
    } catch (error) {
        next(error);
    }
};

exports.getGamesFeatured = async (req, res, next) => {
    try {
        const games = await GameVercion.aggregate([
        {
            $addFields: {
            totalReviews: {
                $add: ["$recommendedCount", "$notRecommendedCount", "$mixedCount"]
            },
            score: { $divide: ["$percentage", 100] }
            }
        },
        {
            $addFields: {
            ranking: {
                $multiply: [
                "$score",
                { $log10: { $add: ["$totalReviews", 1] } }
                ]
            }
            }
        },
        { $sort: { ranking: -1 } },
        { $limit: 10 },

        {
            $project: {
            score: 0,
            totalReviews: 0,
            ranking: 0,
            }
        }
        ]);

        return res.status(200).json(games);
    } catch (error) {
        next(error);
    }
};

exports.getGamesSearch = async (req, res, next) => {
    try {
        const gameVercions = await GameVercion.find({ game_name: { $regex: req.params.search, $options: "i" }});
        res.status(200).send(gameVercions);
    } catch (error) {
        next(error);
    }
};

exports.createGame = async (req, res, next) => {
    try {
        const newGame = new Game(req.body);
        await newGame.save();
        res.status(201).send(newGame);
    } catch (error) {
        next(error);
    }
};

exports.deleteGame = async (req, res, next) => {
    try {
        const game = await Game.findByIdAndDelete(req.params.id);
        if (!game) {
            res.status(404).json({ message: 'Game not found' });
        } else {
            res.status(204).send();
        }
    } catch (error) {
        next(error);
    }
};

exports.getGameVercions = async (req, res, next) => {
    try {
        const gameVercions = await GameVercion.find({ game: req.params.id });
        res.status(200).send(gameVercions);
    } catch (error) {
        next(error);
    }
};

// exports.getConsolesByGame = async (req, res, next) => {
//     try{
//         const games = await GameVercion.find({ game: req.params.id }).lean();
//         const consoles = games.map(g => g.console);
//         res.status(200).send(consoles);
//     }catch(error){
//         next(error);
//     }
// }

exports.getVercion = async (req, res, next) => {
    try {
        const gameVercion = await GameVercion.findById(req.params.id);
        if (!gameVercion) {
            res.status(404).json({ message: 'Game Vercion not found' }).next(error);
        } else {
            res.status(200).send(gameVercion);
        }
    } catch (error) {
        next(error);
    }
};

exports.createGameVercion = async (req, res, next) => {
    try {
        const newGameVercion = new GameVercion(req.body);
        await newGameVercion.save();
        res.status(201).send(newGameVercion);
    } catch (error) {
        next(error);
    }
};

exports.deleteGameVercion = async (req, res, next) => {
    try {
        const gameVercion = await GameVercion.findByIdAndDelete(req.params.id);
        if (!gameVercion) {
            res.status(404).json({ message: 'Game Vercion not found' });
        } else {
            res.status(204).send();
        }
    } catch (error) {
        next(error);
    }
};

exports.getReviewsByGameVercion = async (req, res, next) => {
    try {
        const reviews = await Review.find({ game_version: req.params.id }).populate('usuario', 'nombre image').lean();
        reviews.forEach(review => { review.game_version = null; });
        res.status(200).send(reviews);
    } catch (error) {
        next(error);
    }
};