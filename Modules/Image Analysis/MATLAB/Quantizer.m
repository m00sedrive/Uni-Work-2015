function dct_quantized = Quantizer(mat)

%% Quantizer Matlab Code %%
   % Quantization matrix
	
    blocksize = 8;

    mask = [
	1 1 1 1 0 0 0 0
	1 1 1 0 0 0 0 0
	1 1 0 0 0 0 0 0
	1 0 0 0 0 0 0 0
	0 0 0 0 0 0 0 0
	0 0 0 0 0 0 0 0
	0 0 0 0 0 0 0 0
	0 0 0 0 0 0 0 0];
    
    % Quantize
    dct_quantized = blockproc(mat,[blocksize blocksize],@(block_struct) mask .* block_struct.data);
end