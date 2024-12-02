# Skinology

In this project, we developed a model specifically designed to detect different skin types displayed by users. The model utilizes a transfer learning approach, leveraging InceptionV3's powerful architecture. To improve its accuracy, we applied fine-tuning techniques, refining the performance of the model.
The training process involved 100 epochs for the initial stage, followed by an additional 50 epochs dedicated to fine-tuning.
After that we got this result:

![result](https://github.com/rayhan204/Skinology/blob/3d693b04498c58e1535420d6cb4441d47fcbc164/results/result.png)


![result(1)](https://github.com/rayhan204/Skinology/blob/3d693b04498c58e1535420d6cb4441d47fcbc164/results/result(1).png)

![result(2)](https://github.com/rayhan204/Skinology/blob/3d693b04498c58e1535420d6cb4441d47fcbc164/results/result(2).png)

![result(3)](https://github.com/rayhan204/Skinology/blob/3d693b04498c58e1535420d6cb4441d47fcbc164/results/result(3).png)


### Function Dependencies

| Library      | Version |
| ------------ | ------- |
| Tensorflow   | 2.17.1  |
| Keras        | 3.5.0   |
| Matplotlib   | 3.8.0   |
| NumPy        | 1.26.4  |
| Pandas       | 2.2.2   |
| Scikit-learn | 1.5.2   |


## CNN

<p align="justify"> A basic model is built using Convolutional Neural Networks (CNN) as a foundation. CNN is used to analyze complex visual patterns in facial images, assisting in the classification and identification of different skin types.</p>

## Transfer Learning InceptionV3

<p align="justify">
The transfer learning implementation leverages the renowned InceptionV3 model architecture, widely recognized in image processing. By utilizing the knowledge embedded in pre-trained models, InceptionV3 enables fine-tuning to enhance performance on skin type datasets, ensuring more accurate and effective results.
</p>

## Training, Fine-Tune and Training configurations

<p align="justify">
The model achieved an impressive accuracy of 82%, complemented by an exceptional validation accuracy rate. It was subsequently implemented in TensorFlow format and converted into a TFLite model, optimizing it for seamless deployment on lightweight platforms like mobile devices.
</p>
