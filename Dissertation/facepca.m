% filepath
%path = 'C:\\Users\\user\\Documents\\Uni Work\\Uni-Work-2015\\Dissertation\\Code\\xm2vts\\';

%read 3 face images
face1 = imread('C:\\Users\\user\\Documents\\Uni Work\\Uni-Work-2015\\Dissertation\\Code\\xm2vts\\1_1.bmp');
face2 = imread('C:\\Users\\user\\Documents\\Uni Work\\Uni-Work-2015\\Dissertation\\Code\\xm2vts\\1_2.bmp');
face3 = imread('C:\\Users\\user\\Documents\\Uni Work\\Uni-Work-2015\\Dissertation\\Code\\xm2vts\\1_3.bmp');
face4 = imread('C:\\Users\\user\\Documents\\Uni Work\\Uni-Work-2015\\Dissertation\\Code\\xm2vts\\1_4.bmp');
face5 = imread('C:\\Users\\user\\Documents\\Uni Work\\Uni-Work-2015\\Dissertation\\Code\\xm2vts\\1_5.bmp');
face6 = imread('C:\\Users\\user\\Documents\\Uni Work\\Uni-Work-2015\\Dissertation\\Code\\xm2vts\\1_6.bmp');
face7 = imread('C:\\Users\\user\\Documents\\Uni Work\\Uni-Work-2015\\Dissertation\\Code\\xm2vts\\1_7.bmp');
face8 = imread('C:\\Users\\user\\Documents\\Uni Work\\Uni-Work-2015\\Dissertation\\Code\\xm2vts\\1_8.bmp');

% image dimensions
[irows,icols] = size(face1);

% put the three faces in a matrix. Each column is a face,and rows are the 
% number of pixels
face_matrix = double([face1(:),face2(:),face3(:),face4(:),face5(:),face6(:),face7(:),face8(:)]);
face_matrix = face_matrix'; %each face is an observation as a row, and 
                        %columns are pixels. Required by the pca function

                        
% transpose face_matrix
%B = transpose(face_matrix);
                        
%get average face
% mean image
m = mean(face_matrix,1);  % obtains the mean of each row instead of each column
tmimg=uint8(m); % converts to unsigned 8-bit integer. Values range from 0 to 255
img=reshape(tmimg,icols,irows); % takes the N1*N2x1 vector and creates a N1xN2 matrix
img=img'; 
figure(1); imshow(img);

% apply pca to the face_matrix, returened pc is the principle conponents: 
% each column represents a pc. pcs ranked in the decending way.
[pc, score, value] = pca(face_matrix);

eig_face1=reshape(pc(:,1),55,51); 
eig_face2=reshape(pc(:,2),55,51);
eig_face3=reshape(pc(:,3),55,51); 
eig_face4=reshape(pc(:,4),55,51);
eig_face5=reshape(pc(:,5),55,51); 
eig_face6=reshape(pc(:,6),55,51);
eig_face7=reshape(pc(:,7),55,51);
%eig_face8=reshape(pc(:,8),55,51);

figure(2); imshow(eig_face1,[min(pc(:,1)),max(pc(:,1))]);
figure(3); imshow(eig_face3, [min(pc(:,3)),max(pc(:,3))]);
figure(4); imshow(eig_face4,[min(pc(:,4)),max(pc(:,4))]);
figure(5); imshow(eig_face5,[min(pc(:,5)),max(pc(:,5))]);
figure(6); imshow(eig_face6, [min(pc(:,6)),max(pc(:,6))]);
figure(7); imshow(eig_face7,[min(pc(:,7)),max(pc(:,7))]);
%figure(8); imshow(eig_face8,[min(pc(:,8)),max(pc(:,8))]);


% plot PCA space of the first two PCs: PC1 and PC2
figure(9); plot(pc(1,:),pc(2,:),'.');
