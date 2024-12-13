const skinTypes = {
  oily: [
    {
      id: 1,
      name: "Produksi Sebum yang Berlebihan",
      photo: "https://storage.googleapis.com/skinology-images/images/Excess-Sebum.jpg",
      description: "Sebum berfungsi sebagai pelembap alami, tetapi produksi yang berlebihan menyebabkan kulit terlihat berminyak, berkilau, dan menjadi tempat berkembang biaknya bakteri penyebab jerawat. Faktor seperti genetika, hormon, dan pola makan dapat memengaruhi produksi sebum. Penting untuk menjaga kebersihan wajah dan menggunakan produk yang dapat mengontrol minyak tanpa membuat kulit menjadi kering."
    },
    {
      id: 2,
      name: "Pori-Pori Besar",
      photo: "https://storage.googleapis.com/skinology-images/images/Large-Pores.jpeg",
      description: "Pori-pori besar sering terlihat pada area T-zone (dahi, hidung, dan dagu). Pori-pori ini membesar karena minyak yang berlebihan mengisi pori-pori. Dengan perawatan seperti produk yang mengandung asam salisilat atau niacinamide, pori-pori dapat terlihat lebih kecil."
    },
    {
      id: 3,
      name: "Sering Muncul Jerawat",
      photo: "https://storage.googleapis.com/skinology-images/images/Frequent-Breakouts.jpeg",
      description: "Minyak berlebih yang bercampur dengan kotoran dan sel kulit mati dapat menyumbat pori-pori, menyebabkan jerawat. Perawatan kulit berminyak perlu difokuskan pada pembersihan pori-pori dan mengurangi peradangan."
    },
    {
      id: 4,
      name: "Penampilan Mengkilap",
      photo: "https://storage.googleapis.com/skinology-images/images/Shiny-Appearance.jpg",
      description: "Kulit berminyak cenderung tampak mengkilap sepanjang hari karena produksi minyak yang terus berlangsung. Penggunaan kertas minyak (blotting paper) dan produk berbahan dasar air membantu mengurangi kilap tanpa mengganggu kelembapan alami kulit."
    },
    {
      id: 5,
      name: "Tekstur Lebih Tahan Banting",
      photo: "https://storage.googleapis.com/skinology-images/images/Resilient-Texture.jpg",
      description: "Minyak alami memberikan perlindungan ekstra terhadap kekeringan dan kerusakan akibat polusi atau sinar UV, membuat kulit berminyak lebih tahan terhadap kerutan dan tanda penuaan dini dibandingkan kulit kering."
    }
  ],
  acne: [
    {
      id: 6,
      name: "Whiteheads",
      photo: "https://storage.googleapis.com/skinology-images/images/Whitehead.jpeg",
      description: "Whiteheads muncul ketika pori-pori tersumbat oleh minyak dan sel kulit mati, tetapi tidak terpapar udara sehingga tetap berwarna putih. Perawatan melibatkan eksfoliasi lembut menggunakan AHA/BHA untuk membuka pori-pori."
    },
    {
      id: 7,
      name: "Blackheads",
      photo: "https://storage.googleapis.com/skinology-images/images/Blackhead.jpg",
      description: "Blackheads terjadi ketika pori-pori yang tersumbat terpapar udara, menyebabkan oksidasi sehingga berubah warna menjadi hitam. Gunakan pore strips atau bahan seperti charcoal untuk menghilangkan komedo dengan aman."
    },
    {
      id: 8,
      name: "Pustules",
      photo: "https://storage.googleapis.com/skinology-images/images/Pustules.jpg",
      description: "Pustules adalah jerawat yang biasanya terasa sakit dan menunjukkan peradangan. Kandungan seperti benzoyl peroxide atau tea tree oil dapat membantu meredakan pustules."
    },
    {
      id: 9,
      name: "Nodules",
      photo: "https://storage.googleapis.com/skinology-images/images/Nodules.jpg",
      description: "Nodules adalah jerawat besar yang berkembang jauh di bawah permukaan kulit. Jenis jerawat ini biasanya memerlukan perawatan dokter kulit, seperti injeksi steroid atau terapi oral."
    },
    {
      id: 10,
      name: "Jerawat Kistik (Cystic Acne)",
      photo: "https://storage.googleapis.com/skinology-images/images/Cystic-Acne.jpeg",
      description: "Bentuk jerawat yang paling parah, jerawat kistik, bisa meninggalkan bekas luka permanen. Pengobatan melibatkan konsultasi dengan dokter untuk terapi seperti isotretinoin atau antibiotik oral."
    }
  ],
  dry: [
    {
      id: 11,
      name: "Kulit Mengelupas",
      photo: "https://storage.googleapis.com/skinology-images/images/Flaky-Skin.jpg",
      description: "Kekeringan ekstrem dapat menyebabkan kulit rentan mengelupas, terutama di area seperti pipi dan sekitar mulut. Gunakan pelembap dengan kandungan ceramide atau hyaluronic acid untuk memulihkan kelembapan."
    },
    {
      id: 12,
      name: "Kulit Terasa Kencang",
      photo: "https://storage.googleapis.com/skinology-images/images/Tight-Feeling.jpeg",
      description: "Kulit kering sering terasa kencang setelah mencuci wajah atau terpapar udara dingin. Ini terjadi karena hilangnya lapisan minyak pelindung. Gunakan pembersih wajah lembut yang tidak mengandung sabun untuk mengurangi rasa kencang."
    },
    {
      id: 13,
      name: "Warna Kulit Kusam",
      photo: "https://storage.googleapis.com/skinology-images/images/Dull-Complexion.jpeg",
      description: "Kulit kering tidak mampu memantulkan cahaya dengan baik, membuatnya terlihat kusam. Masker pelembap dan serum dengan kandungan vitamin C dapat membantu mengembalikan kilau alami."
    },
    {
      id: 14,
      name: "Area Sensitif",
      photo: "https://storage.googleapis.com/skinology-images/images/Sensitive-Patches.jpeg",
      description: "Bagian tertentu pada kulit kering sering menjadi merah, iritasi, atau bersisik. Gunakan produk bebas pewangi untuk menghindari iritasi lebih lanjut."
    },
    {
      id: 15,
      name: "Garis Halus",
      photo: "https://storage.googleapis.com/skinology-images/images/Fine-Lines.jpeg",
      description: "Kulit kering cenderung menunjukkan tanda-tanda penuaan lebih cepat. Gunakan pelembap berbasis minyak dan tabir surya setiap hari untuk membantu mengurangi tanda-tanda ini."
    }
  ],
  normal: [
    {
      id: 16,
      name: "Produksi Minyak Seimbang",
      photo: "https://storage.googleapis.com/skinology-images/images/Balanced-Oil-Production.jpg",
      description: "Kulit normal memiliki keseimbangan antara minyak dan kelembapan, sehingga jarang mengalami masalah. Tetap lakukan perawatan rutin untuk menjaga kondisi ini, terutama saat lingkungan berubah."
    },
    {
      id: 17,
      name: "Tekstur Halus",
      photo: "https://storage.googleapis.com/skinology-images/images/Smooth-Texture.jpg",
      description: "Kulit normal memiliki tekstur yang rata, dengan sedikit atau tanpa ketidaksempurnaan. Rutin membersihkan wajah dan menjaga hidrasi penting untuk mempertahankan tekstur ini."
    },
    {
      id: 18,
      name: "Pori-Pori Kecil",
      photo: "https://storage.googleapis.com/skinology-images/images/Small-Pores.jpeg",
      description: "Pada kulit normal, pori-pori hampir tidak terlihat, membuat kulit tampak lebih halus. Hindari produk yang terlalu berat agar tidak menyumbat pori-pori."
    },
    {
      id: 19,
      name: "Warna Kulit Cerah",
      photo: "https://storage.googleapis.com/skinology-images/images/Radiant-Complexion.jpg",
      description: "Warna kulit cerah dan bercahaya adalah tanda kulit yang sehat. Gunakan serum dengan antioksidan untuk melindungi kulit dari radikal bebas."
    },
    {
      id: 20,
      name: "Sensitivitas Minimal",
      photo: "https://storage.googleapis.com/skinology-images/images/Minimal-Sensitivity.jpg",
      description: "Kulit normal jarang bereaksi terhadap produk atau perubahan lingkungan. Namun, tetap berhati-hati saat mencoba produk baru untuk memastikan kulit tetap sehat."
    }
  ]
};

module.exports = skinTypes;
