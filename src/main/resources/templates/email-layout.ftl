<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Movies Email</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }
        .email-container {
            max-width: 600px;
            margin: 20px auto;
            background: #ffffff;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .header {
            text-align: center;
        }
        .content {
            padding: 20px;
            text-align: center;
        }
        .content h2 {
            color: #0056b3;
        }
        .content p {
            color: #333;
            line-height: 1.6;
        }
        .footer {
            background-color: #f4f4f4;
            color: #555;
            font-size: 12px;
            padding: 10px;
            text-align: center;
        }
    </style>
</head>
<body>
<div class="email-container">
    <!-- Header image -->
    <div class="header">
        <img src="cid:logoImage" style="width:100%;max-height:180px;object-fit:contain;">
    </div>


    <!-- English content -->
    <div class="content">
        ${englishContent?c}
    </div>

    <hr/>

    <!-- Arabic content -->
    <div class="content" dir="rtl" style="text-align:right;">
        ${arabicContent?c}
    </div>

    <!-- Footer -->
    <div class="footer">
        <p>&copy; 2025 Movies App. All rights reserved.</p>
    </div>
</div>
</body>
</html>
