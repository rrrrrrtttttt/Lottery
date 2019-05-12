import cv2
import pyzbar.pyzbar as pyzbar
import time

path = "./qr2str.txt"

def decodeDisplay(image):
    barcodes = pyzbar.decode(image)
    text = ""
    for barcode in barcodes:
        # 提取二维码的边界框的位置
        # 画出图像中条形码的边界框
        (x, y, w, h) = barcode.rect
        cv2.rectangle(image, (x, y), (x + w, y + h), (0, 0, 255), 2)
        barcodeData = barcode.data.decode("utf-8")
        barcodeType = barcode.type
        text = barcodeData
        #text = "{} ({})".format(barcodeData, barcodeType)
    return image,text


def detect():
    start = time.time()
    camera = cv2.VideoCapture(0)
    while True:
        # 读取当前帧
        ret, frame = camera.read()
        # 转为灰度图像
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        im = decodeDisplay(gray)
        cv2.waitKey(5)
        cv2.imshow("camera", im[0])

        if im[1].startswith("Chicken, you are too beautiful.") or time.time() - start >= 30:
            fw = open(path,"w")
            fw.write(im[1][31:])
            camera.release()
            break
    camera.release()
    cv2.destroyAllWindows()


if __name__ == '__main__':
    detect()