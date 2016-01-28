
clear ;

% load image
filepath = 'C:\Users\user\Desktop\12347581_10208217147855339_4598098671560992943_n.jpg';
filepath2 = 'C:\Users\user\Desktop\compressedImage.tif';

original = imread(filepath2);
figure,imshow(original);

    
    % convert rgb 2 ycbcr image of type double
	ycbcr_image = im2double(rgb2ycbcr(original));	
	
    % extract Y, Cb and Cr into separate matrices
	Y = ycbcr_image(:,:,1);
	Cb = ycbcr_image(:,:,2);
	Cr = ycbcr_image(:,:,3);

F = dct2(Y);
figure,imshow(F*0.01);

ff = idct(F);
figure,imshow(ff/255);