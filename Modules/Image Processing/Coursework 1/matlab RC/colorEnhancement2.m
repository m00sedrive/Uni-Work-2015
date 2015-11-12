

%% YCBCR Color enhancement %%

% filepaths
img1 = '\\ndrive.rdg.ac.uk\xw009807\My Documents\MATLAB\NoiseFilteredimage.jpg';        % Source image
img2 = '\\ndrive.rdg.ac.uk\xw009807\My Documents\MATLAB\swan3.jpg';                     % Target Image

% read images
targetImage = imread(img1);
sourceImage = imread(img2);

% get image dimensions
[sourceX sourceY sourceZ] = size(targetImage);
[targetX targetY targetZ] = size(sourceImage);

targetImage(:,:,2)=targetImage(:,:,1);
targetImage(:,:,3)=targetImage(:,:,1);
   
% convert to ycbcr color space
space1 = rgb2ycbcr(sourceImage);
space2 = rgb2ycbcr(targetImage);

% convert data to double and store in matrix for source and target image
matSource = double(space1(:,:,1));
matTarget = double(space2(:,:,1));

% get max min of source and target
mat1=max(max(matSource));
mat2=min(min(matSource));
mat3=max(max(matTarget));
mat4=min(min(matTarget));
dim1=mat1-mat2;
dim2=mat3-mat4;

% Normalization
dx1 = matSource;
dx2 = matTarget;
dx1 = (dx1*255)/(255-dim1);
dx2 = (dx2*255)/(255-dim2);
[mx,my,mz] = size(dx2);

%Luminance Comparison   
disp('Please wait..................');
    for i=1:mx
        for j=1:my
             iy=dx2(i,j);
             tmp=abs(dx1-iy);
             ck=min(min(tmp));
             [r,c] = find(tmp==ck);
             ck=isempty(r);
             if (ck~=1)            
                 nimage(i,j,2) = space1(r(1),c(1),2);
                 nimage(i,j,3) = space1(r(1),c(1),3);
                 nimage(i,j,1) = space2(i,j,1);           
             end
         end
    end
    
    rslt=ycbcr2rgb(nimage);
    % display original and esult imagesr
    figure,imshow(uint8(targetImage));
    figure,imshow(uint8(rslt));
    R=uint8(rslt);
    imwrite(R,'ColourEhancedimage.jpg');
