%% Image Compression Matlab Code %%

	% read in image
    filepath2 = 'C:\Users\user\Desktop\12347581_10208217147855339_4598098671560992943_n.jpg';
    filepath3 = 'C:\Users\user\Documents\Uni Work\Uni-Work-2015\Modules\Image Processing\Image Compression\Images\2015_images\eng_LO.png';
    filepath = 'C:\Users\user\Documents\Uni Work\Uni-Work-2015\Modules\Image Processing\Image Compression\Images\2015_images\test_image_HI.png';
    rgb_image = imread(filepath);
    
    % get image info for compression ratios
    rgb_image_info = imfinfo(filepath);
    
    % convert rgb 2 ycbcr image of type double
	ycbcr_image = im2double(rgb2ycbcr(rgb_image));	
   
    % extract Y, Cb and Cr into separate matrices
	Y = ycbcr_image(:,:,1) ;
	Cb = ycbcr_image(:,:,2);
	Cr = ycbcr_image(:,:,3);
   
	% call mapper function to perform DCT
	MappedY = Mapper(Y);
    MappedCb = Mapper(Cb);
	MappedCr = Mapper(Cr);
	     
    % Quantize   
    %QuantY = Quantizer(MappedY);
    %QuantCb = Quantizer(MappedY);
    %QuantCr = Quantizer(MappedCr);
    
    % Run Length Encode
    encoded_Y_data = RLE(QuantY);
    encoded_Cb_data = RLE(QuantCb);
    encoded_Cr_data = RLE(QuantCr);   

 
 %% Image Decompression Matlab code %%
 
    % Run Length Decode
    %decoded_Y_data = RLD(encoded_Y_data, 88);
    %decoded_CB_data = RLD(encoded_Cb_data, 88);
    %decoded_Cr_data = RLD(encoded_Cr_data, 88);
 
    % Dequantize
    dequant_Y = im2double(MappedY);
    dequant_Cb = im2double(MappedCb);
    dequant_Cr = im2double(MappedCr);
    
    % inverse map quantized data
    inv_mapped_Y = InverseMapper(QuantY);
    inv_mapped_Cb = InverseMapper(QuantCb);
    inv_mapped_Cr = InverseMapper(QuantCr);
    
	% reconstruct YCbCr image from remapped data
	decompressed_ycbcr_image = cat(3, inv_mapped_Y, inv_mapped_Cb, inv_mapped_Cr);
    % convert compressed image to rgb
    decompressed_rgb_image = ycbcr2rgb(decompressed_ycbcr_image);
    % display and save decompressed image
    imshow(decompressed_rgb_image);
    imwrite(decompressed_rgb_image,'decompressedImage.jpg');
	filepath4 = 'C:\Users\user\Documents\MATLAB\decompressedImage.jpg';
    compressed_image_info = imfinfo(filepath4);
	
    % signal 2 noise ratio
    % mean squared error
	% compression ratio
	original_image_bytes = rgb_image_info.Width * rgb_image_info.Height * rgb_image_info.BitDepth / 8;
    compressed_image_bytes = compressed_image_info.FileSize;
    compression_ratio = original_image_bytes/compressed_image_bytes;  
    