%%TEST PCA IMPLEMENTATION
clear all;

% final variables
num_images = 7;
image_dims = [48, 64];

% input image
loaded_input_image = imread('C:\\Users\\user\\Documents\\Uni Work\\Uni-Work-2015\\Dissertation\\Code\\xm2vts\\1_8.bmp');

%read 8 face images
face1 = imread('C:\\Users\\user\\Documents\\Uni Work\\Uni-Work-2015\\Dissertation\\Code\\xm2vts\\1_1.bmp');
face2 = imread('C:\\Users\\user\\Documents\\Uni Work\\Uni-Work-2015\\Dissertation\\Code\\xm2vts\\1_2.bmp');
face3 = imread('C:\\Users\\user\\Documents\\Uni Work\\Uni-Work-2015\\Dissertation\\Code\\xm2vts\\1_3.bmp');
face4 = imread('C:\\Users\\user\\Documents\\Uni Work\\Uni-Work-2015\\Dissertation\\Code\\xm2vts\\1_4.bmp');
face5 = imread('C:\\Users\\user\\Documents\\Uni Work\\Uni-Work-2015\\Dissertation\\Code\\xm2vts\\1_5.bmp');
face6 = imread('C:\\Users\\user\\Documents\\Uni Work\\Uni-Work-2015\\Dissertation\\Code\\xm2vts\\1_6.bmp');
face7 = imread('C:\\Users\\user\\Documents\\Uni Work\\Uni-Work-2015\\Dissertation\\Code\\xm2vts\\1_7.bmp');
%face8 = imread('C:\\Users\\user\\Documents\\Uni Work\\Uni-Work-2015\\Dissertation\\Code\\xm2vts\\1_8.bmp');

%resize images into compliable size for algorithm
input_image = imresize(loaded_input_image, image_dims);
f1 = imresize(face1,image_dims);
f2 = imresize(face2,image_dims);
f3 = imresize(face3,image_dims);
f4 = imresize(face4,image_dims);
f5 = imresize(face5,image_dims);
f6 = imresize(face6,image_dims);
f7 = imresize(face7,image_dims);
%f8 = imresize(face8,[44 64]);

% put the image faces in a matrix. Each column is a face and rows are the number of pixels
face_matrix = double([f1(:),f2(:),f3(:),f4(:),f5(:),f6(:),f7(:)]);
%face_matrix = face_matrix';
%each face is an observation as a row, and columns are pixels. Required by the pca function
                        
% find the mean image and mean shifted input
mean_face = mean(face_matrix, 7);
%testMat = repmat(mean_face, 1,num_images);
%shifted_images = face_matrix - repmat(mean_face, 1, num_images);

%calculate the ordered Eigen vectors and values in descending order
[eigen_vectors, scores ,eigen_values] = princomp(face_matrix');

%only retain the top N eigen faces for the calculated principal components
num_eigen_faces = 20;
eigen_vectors = eigen_vectors(:,1:num_eigen_faces);

features = eigen_vectors';
(features);

%testMat = (input_image(:) - mean_face);
features_vectors = eigen_vectors' * (input_image(:) - mean_face);
similarity_score = arrayfun(@(n) 1/ (1 + norm(features(:,n) - features_vectors)), 1:num_images);

% find the image with the highest similarity
[match_score, match_ix] = max(similarity_score);
 
% display the result
figure, imshow([input_image reshape(face_matrix(:,match_ix), image_dims)]);
title(sprintf('matches %s, score %f', filenames(match_ix).name, match_score));

% display the eigenvectors
figure;
for n = 1:num_eigenfaces
    subplot(2, ceil(num_eigenfaces/2), n);
    eigen_vectors = reshape(eigen_vectors(:,n), image_dims);
    imshow(eigen_vectors);
end