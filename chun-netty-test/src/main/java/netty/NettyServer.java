package netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Netty服务端
 * Created by xiegaochun on 2018/1/5.
 */
public class NettyServer {
    private static final int port = 6789; //设置服务端端口


    private static ServerBootstrap b = new ServerBootstrap(); // 引导辅助程序,给他设置一系列参数来绑定端口启动服务

    /**
     * Netty创建全部都是实现自AbstractBootstrap。
     * 客户端的是Bootstrap，服务端的则是    ServerBootstrap。
     **/
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            b.group(bossGroup,workerGroup);
            b.option(ChannelOption.SO_BACKLOG,100);
            b.channel(NioServerSocketChannel.class); // 设置nio类型的channel
            b.handler(new LoggingHandler(LogLevel.INFO));
            b.childHandler(new NettyServerFilter()); //设置过滤器
            // 服务器绑定端口监听
            ChannelFuture f = b.bind(port).sync();  // 通过调用sync同步方法阻塞直到绑定成功
            System.out.println("服务端启动成功...");

            f.channel().closeFuture().sync(); // 应用程序会一直等待，直到channel关闭
        } finally {
             ////关闭EventLoopGroup，释放掉所有资源包括创建的线程
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
