% Run length Encoder
 
function encoded = RLE2(mat)
 
% read matrix into vector
v = reshape( mat.' ,1,numel(mat));

% get vector dimensions
l = length(v);

% create run length counter and array to store result 
run_length = 1;
encoded = [];
 
% iterate through array and run length encode
for i=2:l
    if v(i) == v(i-1)
        run_length = run_length + 1;
    else
        encoded = [encoded v(i-1) run_length];
        run_length = 1;
    end
end
 
if l > 1
    % Add last value and run length to output
    encoded = [encoded v(i) run_length];
else
    % Special case if input is of length 1
    encoded = [v(1) 1];
end

