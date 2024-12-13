# Gunakan image dasar Python
FROM python:3.9-slim

# Set direktori kerja di dalam container
WORKDIR /app

# Menyalin file requirements.txt ke dalam container
COPY requirements.txt /app/

# Install dependencies dari requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

# Menyalin seluruh aplikasi ke dalam container
COPY . /app/

# Install paket Flask-Ngrok dan pyngrok
RUN pip install flask-ngrok pyngrok

# Mengatur environment variable untuk menghindari tampilan buffering log
ENV PYTHONUNBUFFERED=1

# Menentukan variabel lingkungan untuk Flask
ENV PORT 8080

# Menjalankan aplikasi
CMD ["python", "app.py"]