from flask import Flask, request, jsonify
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing.image import img_to_array, load_img
from tensorflow.lite.python.interpreter import Interpreter
from pyngrok import conf, ngrok
import numpy as np
import os
import io

# Instalasi paket
os.system("pip install flask-ngrok pyngrok")

# Konfigurasi Ngrok
conf.get_default().auth_token = "2puw2VKNkASZveQgmihmz5la1PZ_43pWNBQwMor9RvRp2RrPL"

# Inisialisasi Flask
app = Flask(__name__)

# Load TensorFlow Lite model
MODEL_PATH = "converted_model_metadata.tflite"
if not os.path.exists(MODEL_PATH):
    raise FileNotFoundError(f"Model file {MODEL_PATH} not found!")

interpreter = Interpreter(model_path=MODEL_PATH)
interpreter.allocate_tensors()

# Get input and output details
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()
classes = ["berjerawat", "berminyak", "kering", "normal"]

@app.route("/", methods=["GET"])
def home():
    return "InceptionV3 Skinology Model API - Ready for Predictions"

@app.route("/predict", methods=["POST"])
def predict():
    try:
        if 'file' not in request.files:
            return jsonify({"error": "No file uploaded"}), 400

        # Load dan preprocess image
        file = request.files['file']
        img = load_img(io.BytesIO(file.read()), target_size=(224, 224))  # Ubah ke io.BytesIO
        img_array = img_to_array(img)
        img_array = np.expand_dims(img_array, axis=0) / 255.0

        # Perform inference with TensorFlow Lite
        interpreter.set_tensor(input_details[0]['index'], img_array.astype(np.float32))
        interpreter.invoke()
        
        # Prediksi
        predictions = interpreter.get_tensor(output_details[0]['index'])
        predicted_class = classes[np.argmax(predictions)]
        confidence = np.max(predictions)

        return jsonify({"predicted_class": predicted_class, "confidence": float(confidence)})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

PORT = int(os.getenv("PORT", 8080))  # Gunakan port dari variabel lingkungan
if __name__ == "__main__":
    app.run(host="0.0.0.0", port=PORT, debug=True)