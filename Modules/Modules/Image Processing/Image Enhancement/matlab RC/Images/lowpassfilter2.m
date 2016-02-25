

% Signal parameters:
f = [ 440 880 1000 2000 ];      % frequencies
M = 256;                        % signal length
Fs = 5000;                      % sampling rate

% Generate a signal by adding up sinusoids:
x = zeros(1,M); % pre-allocate 'accumulator'
n = 0:(M-1);    % discrete-time grid
for fk = f;
    x = x + sin(2*pi*n*fk/Fs);
end

% Filter parameters:
L = 257;    % filter length
fc = 600;   % cutoff frequency

% Design the filter using the window method:
hsupp = (-(L-1)/2:(L-1)/2);
hideal = (2*fc/Fs)*sinc(2*fc*hsupp/Fs);
h = hamming(L)' .* hideal; % h is our filter