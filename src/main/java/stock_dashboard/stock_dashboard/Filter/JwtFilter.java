package stock_dashboard.stock_dashboard.Filter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import stock_dashboard.stock_dashboard.Util.JwtUtil;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

//    private final JwtUtil jwtUtil;


}
