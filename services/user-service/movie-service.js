const Movie = require('./models/movie.model');

// Create a movie
async function createMovie(movieData) {
  const movie = new Movie(movieData);
  return await movie.save();
}

// Get all movies
async function getAllMovies() {
  return await Movie.find();
}

// Get a single movie by ID
async function getMovieById(id) {
  return await Movie.findById(id);
}

// Update a movie by ID
async function updateMovie(id, updateData) {
  return await Movie.findByIdAndUpdate(id, updateData, { new: true });
}

// Delete a movie by ID
async function deleteMovie(id) {
  return await Movie.findByIdAndDelete(id);
}

module.exports = {
  createMovie,
  getAllMovies,
  getMovieById,
  updateMovie,
  deleteMovie
};
