package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class BeerRestControllerIT extends BaseIT {

  @Test
  void deleteBeerHttpBasic() throws Exception {
    mockMvc
        .perform(
            delete("/api/v1/beer/a805ff12-4818-11ed-b878-0242ac120002")
                .with(httpBasic("spring", "guru")))
        .andExpect(status().is2xxSuccessful());
  }

  @Test
  void deleteBeerNoAuth() throws Exception {
    mockMvc
        .perform(
            delete("/api/v1/beer/a805ff12-4818-11ed-b878-0242ac120002"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void deleteBeer() throws Exception {
    mockMvc
        .perform(
            delete("/api/v1/beer/a805ff12-4818-11ed-b878-0242ac120002")
                .header("Api-Key", "spring")
                .header("Api-Secret", "guru"))
        .andExpect(status().isOk());
  }

  @Test
  void deleteBeerBadCredentials() throws Exception {
    mockMvc
            .perform(
                    delete("/api/v1/beer/a805ff12-4818-11ed-b878-0242ac120002")
                            .header("Api-Key", "spring")
                            .header("Api-Secret", "guruXXX"))
            .andExpect(status().isOk());
  }

  @Test
  void findBeers() throws Exception {
    mockMvc.perform(get("/api/v1/beer/")).andExpect(status().isOk());
  }

  @Test
  void findBeerById() throws Exception {
    mockMvc
        .perform(get("/api/v1/beer/a805ff12-4818-11ed-b878-0242ac120002"))
        .andExpect(status().isOk());
  }

  @Test
  void findBeerByUpc() throws Exception {
    mockMvc.perform(get("/api/v1/beerUpc/8044441222422")).andExpect(status().isOk());
  }
}
