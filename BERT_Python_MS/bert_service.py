from flask import Flask, request, jsonify
from transformers import pipeline

app = Flask(__name__)

# Initialize the BERT pipeline for sentiment analysis
nlp_task = pipeline("sentiment-analysis")

@app.route('/process_text', methods=['POST'])
def process_text():
    data = request.json
    text = data.get('text', '')

    # Use BERT model to analyze text
    result = nlp_task(text)

    return jsonify(result)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
