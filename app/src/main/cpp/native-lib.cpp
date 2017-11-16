#include <termios.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <jni.h>
#include <string>

extern "C" {
    JNIEXPORT jstring JNICALL
    Java_com_example_paha_ccashier_MainActivity_stringFromJNI(
            JNIEnv *env,
            jobject /* this */) {

        std::string hello = "Hello from C++";
        return env->NewStringUTF(hello.c_str());
    }

    JNIEXPORT jint JNICALL Java_com_example_paha_ccashier_MainActivity_openComJNI(
            JNIEnv *env,
            jobject,
            jstring in_PortName) {

            int             hSerial;
            int             Rc;
            char            *PortName;
            jboolean        isCopy;

            env->GetStringUTFChars(in_PortName, &isCopy);

            hSerial = open(PortName, O_RDWR | O_NOCTTY);
            Rc = close(hSerial);

        return 1;
    }
}
