% Run Length Encoder
function encoded = RunLengthEncoder(input)

[height, length] = size(input);

run_length = 1;
encoded = [];
 
for i=2:height
        if input(i) == input(i-1)
               run_length = run_length + 1;
        else
                encoded_row = [encoded input(i-1) run_length];
                run_length = 1;
        end
        
        if height > 1
        % Add last value and run length to output
            encoded_row = [encoded input(i) run_length];
            outp_matrix(r,:) = encoded_row;
        else
            % Special case if input is of length 1
            encoded = [input(1) 1];
        end
end