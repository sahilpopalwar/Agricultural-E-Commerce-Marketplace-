function otpsend(){
const email = document.getElementById('email');
const otpverify = document.getElementsByClassName('otpverify')[0];
Let otp_val = Math.floor(Math.random() * 10000);
Let emailbody = '<h2>Your OTP is </h2>${otp_val}';
Email.send({
SecureToken: "77274213-08a4-4a52-861c-e81db39c644d",
To: email.value,
From: "sahilpopalwar358@gmail.com",
Subject : "TP verification on ",
Body: "Your OTP is" + otp_val,
).then(
message => {
if (message === "OK") {
alert("OTP sent to your email " + email.value);
otpverify.style.display = "flex";
const otp_inp = document.getElementById('otp_inp');
const otp_btn = document.getElementById('otp-btn');
otp_btn.addEventListener('click', () => { if (otp_inp.value == otp_val) {
}
alert("Email address verified...");
        // OTP functionality would go here
        function sendEmailOtp() {
            document.getElementById('emailOtpSection').style.display = 'block';
            startTimer('emailTimer', 300, () => {
                document.getElementById('emailOtpSection').style.display = 'none';
            });
        }
        
        function verifyEmailOtp() {
            const otp = document.getElementById('emailOtp').value;
            if (otp.length === 6) {
                alert('Email OTP verified successfully!');
                document.getElementById('emailOtpSection').style.display = 'none';
            } else {
                alert('Please enter a valid 6-digit OTP');
            }
        }
        
        function sendMobileOtp() {
            document.getElementById('mobileOtpSection').style.display = 'block';
            startTimer('mobileTimer', 300, () => {
                document.getElementById('mobileOtpSection').style.display = 'none';
            });
        }
        
        function verifyMobileOtp() {
            const otp = document.getElementById('mobileOtp').value;
            if (otp.length === 6) {
                alert('Mobile OTP verified successfully!');
                document.getElementById('mobileOtpSection').style.display = 'none';
            } else {
                alert('Please enter a valid 6-digit OTP');
            }
        }
        
        function startTimer(elementId, seconds, callback) {
            const timerElement = document.getElementById(elementId);
            let remaining = seconds;
            
            const interval = setInterval(() => {
                const minutes = Math.floor(remaining / 60);
                const secs = remaining % 60;
                timerElement.textContent = `${minutes}:${secs < 10 ? '0' : ''}${secs}`;
                
                if (remaining <= 0) {
                    clearInterval(interval);
                    if (callback) callback();
                }
                
                remaining--;
            }, 1000);
        }
        
        // Enable register button when all validations pass
        document.getElementById('registerForm').addEventListener('input', function() {
            // Basic validation - in a real app, you'd want more comprehensive checks
            const name = document.getElementById('name').value;
            const email = document.getElementById('email').value;
            const mobile = document.getElementById('mobileNumber').value;
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            const termsChecked = document.getElementById('terms').checked;
            
            const isValid = name && email && mobile && password && confirmPassword && 
                          password === confirmPassword && termsChecked;
            
            document.getElementById('registerButton').disabled = !isValid;
        });
