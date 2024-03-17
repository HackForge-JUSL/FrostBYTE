const express = require("express");
const admin = require("firebase-admin");

// Initialize Firestore and provide service account credentials

var serviceAccount = require("./secret.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();

// Create an Express app
const app = express();


// Define a route for listening to Firestore changes
app.get("/listen-to-changes", async (req, res) => {
  try {
    // Listen to changes in a Firestore collection
    const roomRef = db.collection('rooms').doc('VP8qskyDx9KpYJ7Jkl21');
    const collectionRef = roomRef.collection('messages');
    // const collectionRef = db.collection("rooms"); // Replace 'your-collection' with the actual collection name
    const unsubscribe = collectionRef.onSnapshot((snapshot) => {
      snapshot.docChanges().forEach((change) => {
        if (change.type === "added") {
          console.log("New document:", change.doc.data());
        }
        if (change.type === "modified") {
          console.log("Modified document:", change.doc.data());
        }
        if (change.type === "removed") {
          console.log("Removed document:", change.doc.data());
        }
      });
    });
    res.send("Listening to Firestore changes...");
  } catch (error) {
    console.error("Error listening to changes:", error);
    res.status(500).send("Error listening to Firestore changes");
  }
});

// Start the Express server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
