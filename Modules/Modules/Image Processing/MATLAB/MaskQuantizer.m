function quant_mat = MaskQuantizer(mat)
%MASKQUANTIZER Summary of this function goes here
%   Detailed explanation goes here
        % Quantization matrix
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
    quant_mat = blockproc(mat,[8 8],@(block_struct) mask .* block_struct.data);

end

