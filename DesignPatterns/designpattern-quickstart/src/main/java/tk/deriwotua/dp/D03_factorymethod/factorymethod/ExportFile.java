package tk.deriwotua.dp.D03_factorymethod.factorymethod;

/**
 * 抽象导出
 */
public interface ExportFile {

    /**
     * 所有导出类都需实现导出方法
     * @param data
     * @return
     */
    public boolean export(String data);
}