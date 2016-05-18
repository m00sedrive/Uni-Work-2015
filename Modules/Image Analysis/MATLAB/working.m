%% Image Compression Matlab Code

% dct transform using blocksize 8x8 of Y, Cb,and Cr

    % read in image
	filepath = 'C:\Users\user\Desktop\12347581_10208217147855339_4598098671560992943_n.jpg';
	filepath2 = 'C:\Users\user\Documents\Uni Work\Uni-Work-2015\Modules\Image Processing\Image Compression\Images\2015_images\test_image_HI.png';
    
    rgb_image = imread(filepath2);
    rgb_image_info = imfinfo(filepath2);
    imshow(rgb_image), figure;
    
    % convert rgb 2 ycbcr image of type double
	ycbcr_image = im2double(rgb2ycbcr(rgb_image));	
	
    % extract Y, Cb and Cr into separate matrices
	Y = ycbcr_image(:,:,1) ;
	Cb = ycbcr_image(:,:,2) ;
	Cr = ycbcr_image(:,:,3) ;
    
    % set blocksize
    blocksize = 8;
    
    % create DCT matrix
    dct_matrix = dctmtx(blocksize);	
    
    % create dct transform matrix
    dct = @(block_struct) dct_matrix * block_struct.data * dct_matrix';
    
    % 
    dctY = blockproc(Y,[blocksize blocksize],dct);
    dctCb = blockproc(Cb,[blocksize blocksize],dct);
    dctCr = blockproc(Cr,[blocksize blocksize],dct);	
	
    % Display absolute log of DCT 
	%imshow(log(abs(dctY)),[]), colormap(jet(64)), colorbar;
    
    % Quantization mask matrices
    
    mask16x16 = [
    1 1 1 1 1 1 1 1 0 0 0 0 0 0 0 0
    1 1 1 1 1 1 1 0 0 0 0 0 0 0 0 0
    1 1 1 1 1 1 0 0 0 0 0 0 0 0 0 0
    1 1 1 1 1 0 0 0 0 0 0 0 0 0 0 0
    1 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0
    1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0
    1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
    1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
    0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
    0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
    0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
    0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
    0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
    0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
    0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
    0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0];
    
	mask8x8 = [
	1 1 1 1 0 0 0 0
	1 1 1 0 0 0 0 0
	1 1 0 0 0 0 0 0
	1 0 0 0 0 0 0 0
	0 0 0 0 0 0 0 0
	0 0 0 0 0 0 0 0
	0 0 0 0 0 0 0 0
	0 0 0 0 0 0 0 0];

    mask4x4 = [
    1 1 0 0
    1 0 0 0
    0 0 0 0
    0 0 0 0];

    % set mask
    mask = mask8x8;
    
    % Quantize
    quant_Y = blockproc(dctY,[blocksize blocksize],@(block_struct) mask .* block_struct.data);
    quant_Cb = blockproc(dctCb,[blocksize blocksize],@(block_struct) mask .* block_struct.data);
    quant_Cr = blockproc(dctCr,[blocksize blocksize],@(block_struct) mask .* block_struct.data);
    
    % Run length encode
    encoded_Y = RLE(quant_Y);
    encoded_Cb = RLE(quant_Cb);
    encoded_Cr = RLE(quant_Cr);
    
	% Run length decode
    %decoded_Y = RLD(encoded_Y);
    %decoded_Cb = RLD(encoded_Cb);
    %decoded_Cr = RLD(encoded_Cr);
   
    % inverse dct transform matrix
    inverse_dct = @(block_struct) dct_matrix' * block_struct.data * dct_matrix;
    
    % perform inverse dct
    compressed_Y = blockproc(quant_Y,[blocksize blocksize],inverse_dct);
    compressed_Cb = blockproc(quant_Cb,[blocksize blocksize],inverse_dct);
    compressed_Cr = blockproc(quant_Cr,[blocksize blocksize],inverse_dct);    
    
    % concatate all channels and covert back to RGB
    compressed_ycbcr_image = cat(3, compressed_Y, compressed_Cb, compressed_Cr);
    compressed_rgb_image = ycbcr2rgb(compressed_ycbcr_image);
    
    % save and show image
    imshow(compressed_rgb_image), figure;
    imwrite(compressed_rgb_image,'compressedImage.jpg');
    filepath2 = 'C:\Users\user\Documents\MATLAB\compressedImage.jpg';
    compressed_image_info = imfinfo(filepath2);
	% signal 2 noise ratio
    % mean squared error
	% compression ratio
	original_image_bytes = rgb_image_info.Width * rgb_image_info.Height * rgb_image_info.BitDepth / 8;
    compressed_image_bytes = compressed_image_info.FileSize;
    compression_ratio = original_image_bytes/compressed_image_bytes;
    
    %imshow(log(abs(dctCb)),[]), colormap(jet(64)), colorbar;
    %imshow(log(abs(dctCr)),[]), colormap(jet(64)), colorbar;
    %imshow(log(abs(B)),[]), colormap(jet(64)), colorbar;
	