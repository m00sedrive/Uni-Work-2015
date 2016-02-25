function dequant_lum_mat = LuminanceDequantizer(mat)
%LUMINANCEDEQUANTIZER Summary of this function goes here
%   Detailed explanation goes here

%% luminance Dequantizer Matlab Code %%

    % Luminance Quantization Matrix
    quant_mtrx_lumin = ...
    [16 11 10 16 124 140 151 161;
    12 12 14 19 126 158 160 155;
    14 13 16 24 140 157 169 156;
    14 17 22 29 151 187 180 162;
    18 22 37 56 168 109 103 177;
    24 35 55 64 181 104 113 192;
    49 64 78 87 103 121 120 101;
    72 92 95 98 112 100 103 199] ;

    % Scale quantization matrices based on quality factor
    quality_factor = 75;
    if quality_factor < 50
    quality_scale = floor (5000 / quality_factor);
    else
    quality_scale = 200 - 2 * quality_factor;
    end
    quant_mtrx_lumin = round(quant_mtrx_lumin * quality_scale / 100);

    % Quantize
	dequant_lum_mat = blockproc(mat ,[8 8], @(blockstruct) round(round(blockstruct.data)./quant_mtrx_lumin));
    y = blockproc(y, [8 8], @(blockstruct) blockstruct.data .* qy);

end

