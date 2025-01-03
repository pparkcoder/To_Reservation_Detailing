package detailing.reservation.api;

import detailing.reservation.domain.*;
import detailing.reservation.repository.ReserveRepository;
import detailing.reservation.repository.reserve.query.ReserveFlatDto;
import detailing.reservation.repository.reserve.query.ReserveQueryDto;
import detailing.reservation.repository.reserve.query.ReserveQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ReserveApiController {

    private final ReserveRepository reserveRepository;
    private final ReserveQueryRepository reserveQueryRepository;

    @GetMapping("/api/v1/reserves")
    public List<Reserve> reserveV1(){
        List<Reserve> all = reserveRepository.findAllByString(new ReserveSearch());
        for (Reserve reserve : all) {
            reserve.getMember().getName();
            List<ReserveShop> reserveShops = reserve.getReserveShops();
            reserveShops.stream().forEach(r -> r.getShop().getName());
        }
        return all;
    }

    @GetMapping("/api/v2/reserves")
    public List<ReserveDto> reserveV2(){
        List<Reserve> reserves = reserveRepository.findAllByString(new ReserveSearch());
        List<ReserveDto> collect = reserves.stream()
                .map(r -> new ReserveDto(r))
                .collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/api/v3/reserves")
    public List<ReserveDto> reserveV3(){
        List<Reserve> reserves = reserveRepository.findAllWithShop();

        List<ReserveDto> collect = reserves.stream()
                .map(r -> new ReserveDto(r))
                .collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/api/v3.1/reserves")
    public List<ReserveDto> reserveV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Reserve> reserves = reserveRepository.findAllWithMember(offset, limit);

        List<ReserveDto> collect = reserves.stream()
                .map(r -> new ReserveDto(r))
                .collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/api/v4/reserves")
    public List<ReserveQueryDto> reserveV4(){
        return reserveQueryRepository.findReserveQueryDtos();
    }

    @GetMapping("/api/v5/reserves")
    public List<ReserveQueryDto> reserveV5(){
        return reserveQueryRepository.findAllByDto_optimization();
    }

    @GetMapping("/api/v6/reserves")
    public List<ReserveFlatDto> reserveV6(){
        return reserveQueryRepository.findAllByDto_flat();
    }


    @Data
    static class ReserveDto{

        private Long reserveId;
        private String name;
        private LocalDateTime reserveDate;
        private ReserveStatus reserveStatus;
        private Address address;
        private List<ReserveShopDto> reserveShops;

        public ReserveDto(Reserve reserve) {
            reserveId = reserve.getId();
            name = reserve.getMember().getName();
            reserveDate = reserve.getReserveDate();
            reserveStatus = reserve.getStatus();
            address = reserve.getMember().getAddress();
            reserveShops = reserve.getReserveShops().stream()
                    .map(reserveShop -> new ReserveShopDto(reserveShop))
                    .collect(Collectors.toList());
        }
    }

    @Data
    static class ReserveShopDto {
        private String shopName;
        private int price;
        private int count;
        public ReserveShopDto(ReserveShop reserveShop) {
            shopName = reserveShop.getShop().getName();
            price = reserveShop.getReservePrice();
            count = reserveShop.getReserveCount();
        }
    }
}
