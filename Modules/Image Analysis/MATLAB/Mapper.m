function mapped_image = Mapper(mat)

%% Image Compression Mapper
    % if image matrix not divisble by blocksize pad image with zeros
    % dct transform using blocksize on 2-dimensional matrix
    
    % declare blocksize
    blocksize = 16;
    
    % create DCT matrix
    dct_matrix = dctmtx(blocksize);	
    
    % create dct transform matrix
    dct = @(block_struct) dct_matrix * block_struct.data * dct_matrix';
    
    % calculate dct coefficents
    mapped_image = blockproc(mat,[blocksize blocksize],dct);
    	 
	% imshow(log(abs(dctY)),[]), colormap(jet(64)), colorbar;

end