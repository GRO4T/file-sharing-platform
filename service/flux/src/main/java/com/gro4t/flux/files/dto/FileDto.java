package com.gro4t.flux.files.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileDto {
  private String id;
  private String name;
  private int size;
  private String type;
  private String uploadedBy;
  private String status;
  private LocalDateTime createTime;
}
