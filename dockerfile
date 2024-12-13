# Gunakan image Node.js official
FROM node:16

# Set work directory
WORKDIR /usr/src/app

# Copy package.json dan install dependencies
COPY package*.json ./
RUN npm install

# Copy seluruh kode aplikasi
COPY . .

# Expose port yang digunakan oleh aplikasi
EXPOSE 8080

# Jalankan aplikasi
CMD [ "node", "index.js" ]
