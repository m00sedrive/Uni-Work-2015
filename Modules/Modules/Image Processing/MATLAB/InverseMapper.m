function inverse_mapped = InverseMapper(mat)

%% Image Compression Inverse Mapper

    % declare cksize
    blocksize = 16;

    % create DCT matrix
    dct_matrix = dctmtx(blocksize);
    
    % inverse dct transform matrix
    inverse_dct = @(block_struct) dct_matrix' * block_struct.data * dct_matrix;
    
    % restore data to original format using IDCT
    inverse_mapped = blockproc(mat,[blocksize blocksize],inverse_dct);
    
    
end