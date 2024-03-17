const express = require("express");
const admin = require("firebase-admin");
const { spawn } = require("child_process");
const cors = require("cors");

// Initialize Firestore and provide service account credentials

var serviceAccount = require("./secret.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

const db = admin.firestore();

// Create an Express app
const app = express();

// cors
app.use(cors({ origin: true, credentials: true }));

// init middleware
app.use(express.json());
app.use(express.urlencoded());

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
          const currentTimestamp = Date.now()
          const objectTimestamp = change.doc.data().timestamp
          const diff = currentTimestamp - objectTimestamp
          const twoSec = 2*60*1000

          if (diff < twoSec) {
            console.log(change.doc.id)
            console.log("New document:", change.doc.data());
            const text = change.doc.data().text;
            const pythonProcess = spawn("python", ["./predict.py"], {
              stdio: ["pipe", "pipe", "pipe", "ipc"],
            });

            pythonProcess.stdin.write(JSON.stringify({ text: text }));
            pythonProcess.stdin.end();

            let predictionResult = '';
            pythonProcess.stdout.on("data", (data) => {
              predictionResult += data.toString();
            });

            // Handle errors
            pythonProcess.stderr.on("data", (data) => {
              console.error("Error executing Python script:", data.toString().trim());
              res
                .status(500)
                .json({ error: "An error occurred during model inference." });
            });

              pythonProcess.on("exit", (code) => {
                  if (predictionResult.trim() === "negative") {
                      console.log(predictionResult);
                      // Set the 'removed' flag to true in the Firestore document
                      collectionRef
                          .doc(change.doc.id)
                          .update({ removed: true })
                          .then(() => {
                              console.log("Flag 'removed' set to true for document:", change.doc.id);
                          })
                          .catch((error) => {
                              console.error(
                                  "Error updating document 'removed' flag:",
                                  error
                              );
                          });
                  }
              });
          }

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
