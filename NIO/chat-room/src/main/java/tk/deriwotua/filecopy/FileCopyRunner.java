package tk.deriwotua.filecopy;

import java.io.File;

/**
 * 文件拷贝接口
 */
public interface FileCopyRunner {

    /**
     * source文件拷贝到target文件中
     *
     * @param source
     * @param target
     */
    void copyFile(File source, File target);
}