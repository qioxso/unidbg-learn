package unidbg;
import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.Module;
import com.github.unidbg.arm.backend.Unicorn2Factory;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.dvm.AbstractJni;
import com.github.unidbg.linux.android.dvm.DalvikModule;
import com.github.unidbg.linux.android.dvm.DvmClass;
import com.github.unidbg.linux.android.dvm.DvmObject;
import com.github.unidbg.linux.android.dvm.VM;
import com.github.unidbg.linux.android.dvm.array.ByteArray;
import com.github.unidbg.memory.Memory;
import com.github.unidbg.virtualmodule.android.AndroidModule;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class app extends AbstractJni {

    final AndroidEmulator emulator;
    final VM vm;
    final Module module;
    final DalvikModule dalvikModule;

    public app() {
        // 创建 Android ARM 32位模拟器
        emulator = AndroidEmulatorBuilder.for64Bit()
                .setProcessName("com.vc")
                .addBackendFactory(new Unicorn2Factory(true))
                .build();

        // 获取模拟内存并设置 Android 版本解析器
        Memory memory = emulator.getMemory();
        memory.setLibraryResolver(new AndroidResolver(23)); // 23 代表 Android 版本

        // 创建 Android 虚拟机
        vm = emulator.createDalvikVM();
        vm.setJni(this);
        vm.setVerbose(true);

        // 注册 Android 模块，确保 Unidbg 能正确解析系统库
        new AndroidModule(emulator, vm).register(memory);

        // 加载目标 so 文件
        dalvikModule = vm.loadLibrary(new File("C:\\Users\\Administrator\\Code\\ida\\stu\\libvc.so"), false);
        module = dalvikModule.getModule(); // 获取加载后的模块对象
    }
    public void callAbAddByRegister(int a, int b) {
        dalvikModule.callJNI_OnLoad(emulator);
        DvmClass clazz = vm.resolveClass("com/vc/Native/ab");

        // 创建实例并调用实例方法
        DvmObject<?> instance = clazz.newObject(null);
        int sum = instance.callJniMethodInt(emulator, "add(II)I", a, b);

        System.out.printf("[Register] %d + %d = %d%n", a, b, sum);
    }


    public static void main(String[] args) {
        app tx = new app();
        tx.callAbAddByRegister(1, 7);
    }

}
