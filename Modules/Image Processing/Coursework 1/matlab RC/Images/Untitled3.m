

I =imread('\\ndrive\xw009807\.do_not_delete\desktop.xp\IA assignment\Images\swanNoise.bmp');
%get image dimensions
[M,N] = size(I);

%fourier transform
originalfft = fft2(I);	
shiftedfft = fftshift(originalfft);

fftMagnitude = log(abs(shiftedfft));
% converts out of array index form
fftMagnitude = fftMagnitude - min(fftMagnitude(:));
fftMagnitude = fftMagnitude ./ max(fftMagnitude(:));
%binaries image
binfftMagnitude = im2bw(fftMagnitude, 0.5);
imwrite(fftMagnitude,'fftMagnitude.jpg');

%create filter matrix
filter = zeros([M,N]);

%calculate rect co-ordinates in the middle of filter
rectTopLeft = binfftMagnitude(120,160);      %top left rect
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
imshow(filter);
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

