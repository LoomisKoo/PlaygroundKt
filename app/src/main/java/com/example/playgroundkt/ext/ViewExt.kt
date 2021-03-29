import android.view.View
import java.util.*

/**
 *
 * @ProjectName: PlaygroundKt
 * @Package: com.example.playgroundkt.ext
 * @ClassName: ViewExt
 * @Description: java类作用描述
 * @Author: chunxiong.gu
 * @CreateDate: 2021/3/12 22:00
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/12 22:00
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
var lastClickTime = 0L
fun View.singleClick(during: Long = 500L, callBack: () -> Unit) {
    var now = Date().time
    if (now - lastClickTime > during) {
        callBack()
    }
    lastClickTime = now
}
