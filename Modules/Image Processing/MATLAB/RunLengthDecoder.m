function decoded = RunLengthDecoder(encoded)
 
input_size = size(encoded);
length = input_size(2);
 
index = 1;
decoded = [];
% iterate through the input
while (index <= length)
    % get value which is followed by the run count
    value = encoded(index);
    run_length = encoded(index + 1);
    for i=1:run_length
        % loop adding 'value' to output 'run_length' times
        decoded = [decoded value];
    end
    % put index at next value element (odd element)
    index = index + 2;
end