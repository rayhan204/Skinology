const express = require("express");
const skinTypes = require("./data/skinTypes");
const app = express();
const PORT = process.env.PORT || 8080;

// Root Endpoint
app.get("/", (req, res) => {
  res.json({ message: "Welcome to the Skinology Articles API!" });
});

// Get All Skin Types
app.get("/skinTypes", (req, res) => {
  res.json(skinTypes);
});

// Get Skin Type by Category
app.get("/skinTypes/:type", (req, res) => {
  const { type } = req.params;
  if (skinTypes[type]) {
    res.json(skinTypes[type]);
  } else {
    res.status(404).json({ error: "Skin type category not found." });
  }
});

// Get Skin Type Detail by Category and ID
app.get("/skinTypes/:type/:id", (req, res) => {
  const { type, id } = req.params;

  if (!skinTypes[type]) {
    return res.status(404).json({ error: "Skin type category not found." });
  }

  const skinTypeDetail = skinTypes[type].find(item => item.id === parseInt(id));

  if (!skinTypeDetail) {
    return res.status(404).json({ error: "Skin type with the given ID not found." });
  }

  res.json(skinTypeDetail);
});

// Start Server
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
