
%Periodic noise removal

I = imread('\\ndrive\xw009807\.do_not_delete\desktop.xp\IA assignment\Images\swanNoise.bmp');
originalfft = fft2(I);
shiftedfft = fftshift(originalfft);
fftmag = log(1 + abs(shiftedfft));
%imshow(fftmag,[]);
fftphase = angle(originalfft);
%imshow(fftphase);
filter = mat2gray(log(1+abs(shiftedfft)));
%imshow(magnitude, []);
%plot(fftmag);


