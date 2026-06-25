<?php
session_start();

$captcha_text = substr(str_shuffle("0123456789abcdefghijklmnopqrstuvwxyz"), 0, 6);

$_SESSION['captcha_text'] = $captcha_text;

$image = imagecreatetruecolor(120, 40);

$bg_color = imagecolorallocate($image, 255, 255, 255);
$text_color = imagecolorallocate($image, 0, 0, 0);

imagefilledrectangle($image, 0, 0, 120, 40, $bg_color);

imagestring($image, 5, 30, 12, $captcha_text, $text_color);

header('Content-type: image/png');

imagepng($image);

imagedestroy($image);
?>
