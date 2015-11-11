
% image filepath
path = '\\ndrive\xw009807\.do_not_delete\desktop.xp\IA assignment\Images\swanNoise.bmp';
% read image
I =imread(path);
% get image dimensions
[M,N] = size(I);

% fourier transform
originalfft = fft2(I);	
% shift fourier data so that lowest frequencies are centered
shiftedfft = fftshift(originalfft);
% calculate fourier magnitude
fftMagnitude = log(abs(shiftedfft));

% converts out of array index form
%fftMagnitude = fftMagnitude - min(fftMagnitude(:));
%fftMagnitude = fftMagnitude ./ max(fftMagnitude(:));

%binaries image
binfftMagnitude = im2bw(fftMagnitude, 0.5);
%imwrite(fftMagnitude,'fftMagnitude.jpg');

%create filter matrix
filter = zeros([M,N]);

%calculate rect co-ordinates in the middle of filter
fftMagnitude(38,51) = 0;
fftMagnitude(38,151) = 0;
fftMagnitude(38,251) = 0 ;
fftMagnitude(38,351) = 0 ;
fftMagnitude(38,451) = 0 ;

fftMagnitude(113,51) = 0 ;
fftMagnitude(113,151) = 0 ;
fftMagnitude(113,251) = 0 ;
fftMagnitude(113,351) = 0 ;
fftMagnitude(113,451) = 0 ;

fftMagnitude(188,51) = 0 ;
fftMagnitude(188,151) = 0 ;
fftMagnitude(188,351) = 0 ;
fftMagnitude(188,451) = 0 ;

fftMagnitude(263,51) = 0 ;
fftMagnitude(263,151) = 0 ;
fftMagnitude(263,251) = 0 ;
fftMagnitude(263,351) = 0 ;
fftMagnitude(263,451) = 0 ;

fftMagnitude(338,51) = 0 ;
fftMagnitude(338,151) = 0 ;
fftMagnitude(338,251) = 0 ;
fftMagnitude(338,351) = 0 ;
fftMagnitude(338,451) = 0 ;


inverseFFT = mat2gray(abs(ifft2(fftMagnitude .* shiftedfft)));
SPfilterInverseFFT = medfilt2(IFT, [3 3]);
imshow(SPfilterInverseFFT);
imwrite(SPfilterInverseFFT,'NoiseFilteredimage.jpg');




rectTopright = binfftMagnitude(120,340);     %top right rect
rectBottomLeft = binfftMagnitude(250,160);   %bottom left rect
rectBottomRight = binfftMagnitude(250,340);  %bottom right rect

% find all white pixels in binary image not in rect
% loop over all rows and columns
for r=1:size(M)
    for c=1:size(N)
        
        % get pixel value
        pixel=binfftMagnitude(r,c);      

              % define rect to be ignored
              %if r < 120 || r > 250;
                   %if c < 160 || c > 340;
                       % if pixel is white
                       if pixel==1
                            %set detected coordinate in filter to 0
                            filter(r,c) = 1;
                       end
                   %end
              %end
          
          % save new pixel value in thresholded image
          %filter(i,j)=new_pixel;
    end
end

fftPhase = angle(originalfft);

%rectWidth = 10;
%rectHeight = 5;		
%kernel = ones(rectWidth, rectHeight)/(rectHeight * rectWidth);
%filteredImage = conv2(double(I), kernel, 'same');
%figure(2),imshow(filteredImage,[]);

    %get fourier transform data
    %store fourier transform data in matrix FT
    %create filter in matrix
    %store filter data in matrix F
    %read values of each element inmatrix F
    %if value > 0 then mulitply by 0
    %

   figure,
%imshow(fftMagnitude);
%imshow(fftMagnitude, []);
%imshow(log(abs(shiftedfft + 1)), []);
%subplot(2,2,1);
%title('Original Image');
%imshow(I);
%subplot(2,2,2);
%title('Fourier Transform');
%imshow(originalfft);
%subplot(2,2,3);
%title('Fourier Magnitude');
%imshow(fftMagnitude, []);
%subplot(2,2,3);
%title('Fourier Phase');
%imshow(fftPhase);

