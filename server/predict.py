from transformers import BertTokenizer, BertModel
import torch
import json
import sys
from torch import nn

bert_model_name = 'bert-base-uncased'

class BERTClassifier(nn.Module):
    def __init__(self, bert_model_name, num_classes):
        super(BERTClassifier, self).__init__()
        self.bert = BertModel.from_pretrained(bert_model_name)
        self.dropout = nn.Dropout(0.1)
        self.fc = nn.Linear(self.bert.config.hidden_size, num_classes)

    def forward(self, input_ids, attention_mask):
        outputs = self.bert(input_ids=input_ids, attention_mask=attention_mask)
        pooled_output = outputs.pooler_output
        x = self.dropout(pooled_output)
        logits = self.fc(x)
        return logits

tokenizer = BertTokenizer.from_pretrained(bert_model_name)
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

def predict_sentiment(text, model, tokenizer, device, max_length=128):
    model.eval()
    encoding = tokenizer(text, return_tensors='pt', max_length=max_length, padding='max_length', truncation=True)
    input_ids = encoding['input_ids'].to(device)
    attention_mask = encoding['attention_mask'].to(device)

    with torch.no_grad():
        outputs = model(input_ids=input_ids, attention_mask=attention_mask)
        _, preds = torch.max(outputs, dim=1)
        return "positive" if preds.item() == 1 else "negative"


model = BERTClassifier(bert_model_name, 2).to(device)
model.load_state_dict(torch.load('./bert_classifier (1).pth', map_location=torch.device('cpu')))

input_data_json = sys.stdin.read().strip()
input_data = json.loads(input_data_json)
test_text = input_data['text']

prediction = predict_sentiment(test_text, model, tokenizer, device);

prediction_result = {"prediction": prediction}

print(json.dumps(prediction_result))
