package ruby.bamboo.block;

public interface ICustomState {
    // IStateMapperはClientOnlyのため、サーバー側でロードできない
    public Object getCustomState();
}
