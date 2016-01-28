function quantized_image = ChrominanceQuantizer(mat)
%CHROMINANCEQUANTIZER Summary of this function goes here
%   Detailed explanation goes here

    % Chrominance Quantization Matrix
    quant_mtrx_chrom = ...
    [17 18 24 47 99 99 99 99;
    18 21 26 66 99 99 99 99;
    24 26 56 99 99 99 99 99;
    47 66 99 99 99 99 99 99;
    99 99 99 99 99 99 99 99;
    99 99 99 99 99 99 99 99;
    99 99 99 99 99 99 99 99;
    99 99 99 99 99 99 99 99];

    % Scale quantization matrices based on quality factor
    quality_factor = 75;
    if quality_factor < 50
    quality_scale = floor (5000 / quality_factor);
    else
    quality_scale = 200 - 2 * quality_factor;
    end
    quant_mtrx_chrom = round(quant_mtrx_chrom * quality_scale / 100);
    
    % Quantize
	quantized_image = blockproc(mat ,[8 8], @(blockstruct) round(round(blockstruct.data)./quant_mtrx_chrom));
    
end

