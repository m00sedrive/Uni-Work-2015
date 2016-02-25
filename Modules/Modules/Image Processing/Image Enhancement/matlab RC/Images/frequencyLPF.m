

% simple implementation of frequency domain filters
%clear all
%read input image
%dim=imread('\\ndrive\xw009807\.do_not_delete\desktop.xp\IA assignment\Images\swanNoise.bmp');
dim = imread('\\ndrive.rdg.ac.uk\xw009807\My Documents\MATLAB\medianFilteredImage.jpg');
cim=double(dim);
[r,c]=size(cim);

r1=2*r;
c1=2*c;

pim=zeros((r1),(c1));
kim=zeros((r1),(c1));

%padding
for i=1:r
    for j=1:c
   pim(i,j)=cim(i,j);
    end
end

%center the transform
for i=1:r
    for j=1:c
   kim(i,j)=pim(i,j)*((-1)^(i+j));
    end
end

%2D fft
fim=fft2(kim);

%n=1; %order for butterworth filter
thresh=50; % cutoff radius in frequency domain for filters

% create and apply low-pass filter
[r,c]=size(fim);
d0=thresh;

d=zeros(r,c);
h=zeros(r,c);

for i=1:r
    for j=1:c
     d(i,j)=  sqrt( (i-(r/2))^2 + (j-(c/2))^2);
    end
end

for i=1:r
    for j=1:c
      h(i,j)= exp ( -( (d(i,j)^2)/(2*(d0^2)) ) );
    end
end


for i=1:r
    for j=1:c
    res(i,j)=(h(i,j))*fim(i,j);

    end
end

%him=blpf(fim,thresh,n); % butterworth low pass filter


% % function calls for high pass filters
% him=ghp(fim,thresh); % gaussian low pass filter
%  him=bhp(fim,thresh,n);  %butterworth high pass filter

% % function call for high boost filtering
% him=hbg(fim,thresh);  % using gaussian high pass filter
%  him=hbb(fim,thresh,n);  % using butterworth high pass filter


%inverse 2D fft
 ifim=ifft2(res);
 
 %  imshow(res);
 % issue is after here
 
for i=1:r1
    for j=1:c1
   ifim(i,j)=ifim(i,j)*((-1)^(i+j));
    end
end

%imshow(ifim);

% removing the padding
for i=1:r
    for j=1:c
   rim(i,j)=ifim(i,j);
    end
end

%imshow(rim);

% retaining the real parts of the matrix
rim=real(rim);
rim=uint8(rim);

figure, imshow(rim);

 %figure;
 %subplot(2,3,1);imshow(dim);title('Original image');
 %subplot(2,3,2);imshow(uint8(kim));title('Padding');
 %subplot(2,3,3);imshow(uint8(fim));title('Transform centering');
 %subplot(2,3,4);imshow(uint8(him));title('Fourier Transform');
 %subplot(2,3,5);imshow(uint8(ifim));title('Inverse fourier transform');
 %subplot(2,3,6);imshow(uint8(rim));title('Cropped image');
